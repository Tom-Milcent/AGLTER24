import os
import argparse
import re
import ast
import json
import yaml
import subprocess
import graphviz
import os
import shutil

from collections import defaultdict

# Fonction pour analyser le fichier et extraire les classes et fonctions
class CodeAnalyzer(ast.NodeVisitor):
    def __init__(self):
        self.classes = []
        self.functions = []

    def visit_ClassDef(self, node):
        self.classes.append(node.name)
        self.generic_visit(node)

    def visit_FunctionDef(self, node):
        self.functions.append(node.name)
        self.generic_visit(node)

# Fonction pour générer l'arborescence du projet avec détails sur les fichiers
def generer_arborescence(dossier, exclusions):
    arborescence = ""
    for root, dirs, files in os.walk(dossier):
        dirs[:] = [d for d in dirs if d not in exclusions]
        niveau = root.replace(dossier, "").count(os.sep)
        indent = " " * 4 * niveau
        arborescence += f"{indent}{os.path.basename(root)}/\n"
        sous_indent = " " * 4 * (niveau + 1)
        for fichier in files:
            if fichier not in exclusions:
                chemin_fichier = os.path.join(root, fichier)
                try:
                    taille = os.path.getsize(chemin_fichier)
                    permissions = oct(os.stat(chemin_fichier).st_mode)[-3:]
                    arborescence += f"{sous_indent}{fichier} (taille: {taille} octets, permissions: {permissions})\n"
                except Exception as e:
                    arborescence += f"{sous_indent}{fichier} (informations non disponibles: {e})\n"
    return arborescence

# Fonction utilitaire pour vérifier si un fichier est pertinent
def est_pertinent(fichier, extensions, exclusions):
    extension = os.path.splitext(fichier)[1]
    if extension not in extensions or fichier in exclusions:
        return False
    try:
        with open(fichier, 'r', encoding='utf-8') as f:
            contenu = f.read()
            mots_cles = ["main", "import", "class", "def", "function"]
            for mot in mots_cles:
                if mot in contenu:
                    return True
    except Exception as e:
        return False
    return False

# Extraction des métadonnées d'un fichier
def extraire_metadonnees(fichier):
    try:
        taille = os.path.getsize(fichier)
        permissions = oct(os.stat(fichier).st_mode)[-3:]
        return f"Taille: {taille} octets, Permissions: {permissions}"
    except Exception as e:
        return f"Informations non disponibles: {e}"

# Extraction des informations clés
def extraire_informations_cles(contenu, chemin_fichier):
    informations_cles = []
    if chemin_fichier.endswith(('.py', '.java', '.js', '.ts', '.cpp', '.c', '.hpp', '.h', '.cs', '.php', '.rb', '.go', '.swift')):
        if chemin_fichier.endswith('.py'):
            analyzer = CodeAnalyzer()
            try:
                arbre = ast.parse(contenu)
                analyzer.visit(arbre)
                if analyzer.classes:
                    informations_cles.append(f"Classes trouvées: {', '.join(analyzer.classes)}")
                if analyzer.functions:
                    informations_cles.append(f"Fonctions trouvées: {', '.join(analyzer.functions)}")
            except Exception as e:
                informations_cles.append(f"Erreur d'analyse AST: {e}")
        else:
            classes = re.findall(r'class\s+(\w+)', contenu)
            if classes:
                informations_cles.append(f"Classes trouvées: {', '.join(classes)}")
            fonctions = re.findall(r'def\s+(\w+)|function\s+(\w+)', contenu)
            if fonctions:
                fonctions_noms = [f[0] if f[0] else f[1] for f in fonctions]
                informations_cles.append(f"Fonctions trouvées: {', '.join(fonctions_noms)}")
        imports = re.findall(r'import\s+([\w\.]+)|from\s+([\w\.]+)\s+import', contenu)
        imports_noms = [imp[0] if imp[0] else imp[1] for imp in imports]
        if imports_noms:
            informations_cles.append(f"Dépendances: {', '.join(imports_noms)}")
    return informations_cles

# Fonction pour générer un graphe des dépendances
def generer_graphe_dependances(dossier, exclusions, dossier_output):
    os.environ["PATH"] += os.pathsep + r"C:\\Program Files\\Graphviz\\bin"

    graphe = graphviz.Digraph('Dépendances', format='svg')  # Utiliser SVG pour un format plus adapté à ChatGPT
    dependencies = defaultdict(set)

    for root, _, files in os.walk(dossier):
        for fichier in files:
            chemin_fichier = os.path.join(root, fichier)
            if chemin_fichier.endswith(('.py', '.java', '.js', '.ts', '.cpp', '.c', '.cs', '.php', '.rb', '.go', '.swift')):
                try:
                    with open(chemin_fichier, 'r', encoding='utf-8') as f:
                        contenu = f.read()
                        imports = re.findall(r'import\s+([\w\.]+)|from\s+([\w\.]+)\s+import', contenu)
                        imports_noms = [imp[0] if imp[0] else imp[1] for imp in imports]
                        for imp in imports_noms:
                            dependencies[os.path.basename(chemin_fichier)].add(imp)
                except Exception as e:
                    print(f"Erreur lors de la lecture du fichier {chemin_fichier}: {e}")
                    continue

    for fichier, deps in dependencies.items():
        for dep in deps:
            graphe.edge(fichier, dep)

    graphe.render(os.path.join(dossier_output, 'graph_dependances'), engine='dot')

# Fonction principale
def main():
    parser = argparse.ArgumentParser(description="Transcription de fichiers pertinents pour comprendre un projet.")
    parser.add_argument("--extensions", nargs="+", default=[
        '.py', '.txt', '.md', '.json', '.cfg', '.ini', '.yml', '.yaml',
        '.html', '.css', '.js', '.java', '.cpp', '.c', '.hpp', '.h', '.cs',
        '.php', '.rb', '.go', '.ts', '.xml', '.sh', '.bat', '.swift'
    ], help="Extensions de fichiers à inclure (par défaut : extensions de programmation classiques)."
    )
    parser.add_argument("--exclusions", nargs="+", default=[
        '__pycache__', '.git', 'venv', 'dist', 'build', '.idea', 'target', 'mvnw', 'mvnw.cmd'
    ], help="Fichiers et dossiers à exclure (par défaut : fichiers non pertinents)."
    )
    parser.add_argument("--taille-maximale", type=int, default=1,
                        help="Taille maximale des fichiers à inclure en Mo (par défaut : 1 Mo)."
    )
    parser.add_argument("--dossier-source", type=str, default=os.path.abspath(os.path.dirname(__file__)),
                        help="Dossier source à parcourir (par défaut : répertoire du script)."
    )
    parser.add_argument("--fichier-destination", type=str, default='output/#Transcription.txt',
                        help="Nom du fichier de destination pour la transcription (par défaut : output/#Transcription.txt)."
    )

    args = parser.parse_args()

    # Créer le dossier output s'il n'existe pas
    dossier_output = os.path.join(args.dossier_source, 'output')
    if not os.path.exists(dossier_output):
        os.makedirs(dossier_output)

    # Ajouter le dossier output aux exclusions
    args.exclusions.append('output')

    fichier_destination = os.path.join(dossier_output, os.path.basename(args.fichier_destination))

    with open(fichier_destination, 'w', encoding='utf-8') as f_dest:
        f_dest.write("Arborescence du projet :\n")
        arborescence = generer_arborescence(args.dossier_source, args.exclusions)
        f_dest.write(arborescence + "\n")

        taille_maximale = args.taille_maximale * 1024 * 1024

        for root, dirs, files in os.walk(args.dossier_source):
            dirs[:] = [d for d in dirs if d not in args.exclusions]
            for fichier in files:
                chemin_fichier = os.path.join(root, fichier)
                if est_pertinent(chemin_fichier, args.extensions, args.exclusions) and os.path.getsize(chemin_fichier) <= taille_maximale:
                    try:
                        with open(chemin_fichier, 'r', encoding='utf-8') as f_source:
                            contenu = f_source.read()
                        metadonnees = extraire_metadonnees(chemin_fichier)
                        informations_cles = extraire_informations_cles(contenu, chemin_fichier)
                        informations_cles_str = "\n".join(informations_cles)

                        f_dest.write(f"{chemin_fichier} ({metadonnees}) :\n")
                        if informations_cles:
                            f_dest.write(f"Informations clés :\n{informations_cles_str}\n")
                        f_dest.write(f"```\n{contenu}\n```\n\n")
                        print(f"Ajouté : {chemin_fichier}")
                    except Exception as e:
                        print(f"Impossible de lire {chemin_fichier}: {e}")

        generer_graphe_dependances(args.dossier_source, args.exclusions, dossier_output)

if __name__ == "__main__":
    main()
