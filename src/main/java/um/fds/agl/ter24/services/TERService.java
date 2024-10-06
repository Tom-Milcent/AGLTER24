package um.fds.agl.ter24.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import um.fds.agl.ter24.entities.TER;
import um.fds.agl.ter24.repositories.TERRepository;

import java.util.Optional;

@Service
public class TERService {


    @Autowired
    private TERRepository terRepository;

    public Optional<TER> getTER(final Long id) {
        return terRepository.findById(id);
    }

    public Iterable<TER> getTERs() {
        return terRepository.findAll();
    }

    public void deleteTER(final Long id) {
        terRepository.deleteById(id);
    }

    public TER saveTER(TER ter) {
        return terRepository.save(ter);
    }

    public Optional<TER> findById(long id) {
        return terRepository.findById(id);
    }
}
