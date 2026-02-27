package com.thiagoarend.parking_spot_management_api.service;

import com.thiagoarend.parking_spot_management_api.entity.User;
import com.thiagoarend.parking_spot_management_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service // transforma a classe em um bean gerenciado pelo spring
public class UserService {
    private final UserRepository userRepository; // injeção por construtor é feita na construção do objeto; é gerenciada pelo spring

    @Transactional // spring toma conta da transação; cuida do recurso (abrir, gerenciar, fechar transação)
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true) // método exclusivo para consulta no banco de dados
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found.")
        );
    }

    @Transactional
    public User updatePassword(Long id, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("New password does not match password confirmation.");
        }

        User persistedUser = findById(id); // persistedUser esta em estado 'persistente', no qual o hibernate controla o objeto, por isso o update nao é necessário e o set da senha no objeto resolve
        if (!currentPassword.equals(persistedUser.getPassword())) {
            throw new RuntimeException("Wrong current password value.");
        }

        persistedUser.setPassword(newPassword); // poderia salvar manualmente em problemas, mas o hibernate já faz isso
        return persistedUser;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
