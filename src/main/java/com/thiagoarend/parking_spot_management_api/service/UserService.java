package com.thiagoarend.parking_spot_management_api.service;

import com.thiagoarend.parking_spot_management_api.entity.User;
import com.thiagoarend.parking_spot_management_api.exception.EntityNotFoundException;
import com.thiagoarend.parking_spot_management_api.exception.PasswordInvalidException;
import com.thiagoarend.parking_spot_management_api.exception.UsernameUniqueViolationException;
import com.thiagoarend.parking_spot_management_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service // transforma a classe em um bean gerenciado pelo spring
public class UserService {
    private final UserRepository userRepository; // injeção por construtor é feita na construção do objeto; é gerenciada pelo spring

    @Transactional // spring toma conta da transação; cuida do recurso (abrir, gerenciar, fechar transação)
    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch(DataIntegrityViolationException ex) {
            throw new UsernameUniqueViolationException(String.format("Username '%s' is already in use.", user.getUsername()));
        }
    }

    @Transactional(readOnly = true) // método exclusivo para consulta no banco de dados
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id '%s' was not found.", id))
        );
    }

    @Transactional
    public User updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new PasswordInvalidException("Password confirmation does not match inputted password.");
        }

        User persistedUser = findById(id); // persistedUser esta em estado 'persistente', no qual o hibernate controla o objeto, por isso o update nao é necessário e o set da senha no objeto resolve
        if (!currentPassword.equals(persistedUser.getPassword())) {
            throw new PasswordInvalidException("Wrong current password value.");
        }

        persistedUser.setPassword(newPassword); // poderia salvar manualmente em problemas, mas o hibernate já faz isso
        return persistedUser;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
