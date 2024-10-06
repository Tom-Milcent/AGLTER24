import os
import argparse

# Fonction pour générer l'arborescence du projet
def generer_arborescence(dossier, exclusions):
    arborescence = ""
    for root, dirs, files in os.walk(dossier):
        # Exclure les dossiers non pertinents
        dirs[:] = [d for d in dirs if d not in exclusions]
        niveau = root.replace(dossier, "").count(os.sep)
        indent = " " * 4 * niveau
        arborescence += f"{indent}{os.path.basename(root)}/\n"
        sous_indent = " " * 4 * (niveau + 1)
        for fichier in files:
            if fichier not in exclusions:
                arborescence += f"{sous_indent}{fichier}\n"
    return arborescence

# Fonction utilitaire pour vérifier si un fichier est pertinent
def est_pertinent(fichier, extensions, exclusions):
    extension = os.path.splitext(fichier)[1]
    return extension in extensions and fichier not in exclusions

def main():
    # Parseur d'arguments pour personnaliser le comportement
    parser = argparse.ArgumentParser(description="Transcription de fichiers pertinents pour comprendre un projet.")
    
    # Arguments pour les extensions à inclure et les fichiers/dossiers à exclure
    parser.add_argument("--extensions", nargs="+", default=[
        '.py', '.txt', '.md', '.json', '.cfg', '.ini', '.yml', '.yaml',
        '.html', '.css', '.js', '.java', '.cpp', '.c', '.hpp', '.h', '.cs',
        '.php', '.rb', '.go', '.ts', '.xml', '.sh', '.bat', '.swift'
    ], help="Extensions de fichiers à inclure (par défaut : extensions de programmation classiques).")

    parser.add_argument("--exclusions", nargs="+", default=[
        '#Transcription.py', '#Transcription.txt', '__pycache__', '.git', 'venv', 'dist', 'build'
    ], help="Fichiers et dossiers à exclure (par défaut : fichiers non pertinents).")
    
    parser.add_argument("--taille-maximale", type=int, default=1, 
                        help="Taille maximale des fichiers à inclure en Mo (par défaut : 1 Mo).")
    
    # Chemins pour le dossier source et fichier de destination
    parser.add_argument("--dossier-source", type=str, default=os.path.abspath(os.path.dirname(__file__)),
                        help="Dossier source à parcourir (par défaut : répertoire du script).")
    
    parser.add_argument("--fichier-destination", type=str, default='#Transcription.txt',
                        help="Nom du fichier de destination pour la transcription (par défaut : #Transcription.txt).")

    args = parser.parse_args()

    # Chemin absolu du fichier de destination
    fichier_destination = os.path.join(args.dossier_source, args.fichier_destination)

    # Ouvre le fichier de destination en mode écriture
    with open(fichier_destination, 'w', encoding='utf-8') as f_dest:
        # Ajoute l'arborescence du projet au début du fichier
        f_dest.write("Arborescence du projet :\n")
        arborescence = generer_arborescence(args.dossier_source, args.exclusions)
        f_dest.write(arborescence + "\n")
        
        # Taille maximale des fichiers en octets
        taille_maximale = args.taille_maximale * 1024 * 1024  # Convertir en octets

        # Parcourt tous les dossiers et fichiers de manière récursive pour la transcription
        for root, dirs, files in os.walk(args.dossier_source):
            # Exclure les dossiers non pertinents
            dirs[:] = [d for d in dirs if d not in args.exclusions]
            
            for fichier in files:
                chemin_fichier = os.path.join(root, fichier)
                
                # Vérifie si le fichier est pertinent et ne dépasse pas la taille maximale
                if est_pertinent(fichier, args.extensions, args.exclusions) and os.path.getsize(chemin_fichier) <= taille_maximale:
                    try:
                        with open(chemin_fichier, 'r', encoding='utf-8') as f_source:
                            contenu = f_source.read()
                        # Écrit le chemin du fichier et son contenu formaté dans le fichier de destination
                        f_dest.write(f"{chemin_fichier} :\n```\n{contenu}\n```\n\n")
                        print(f"Ajouté : {chemin_fichier}")
                    except Exception as e:
                        # Si une erreur se produit (par exemple, un fichier non lisible), on l'ignore
                        print(f"Impossible de lire {chemin_fichier}: {e}")

if __name__ == "__main__":
    main()
