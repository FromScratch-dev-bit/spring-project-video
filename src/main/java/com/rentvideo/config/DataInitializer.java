package com.rentvideo.config;

import com.rentvideo.model.Role;
import com.rentvideo.model.User;
import com.rentvideo.model.Video;
import com.rentvideo.repository.UserRepository;
import com.rentvideo.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        initializeUsers();
        initializeVideos();
    }
    
    private void initializeUsers() {
        if (userRepository.count() == 0) {
            // Create Admin User
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("Admin User");
            admin.setEmail("admin@rentvideo.com");
            admin.setPhoneNumber("+1234567890");
            admin.setRole(Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
            
            // Create Regular User
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setFullName("John Doe");
            user.setEmail("user@rentvideo.com");
            user.setPhoneNumber("+0987654321");
            user.setRole(Role.USER);
            user.setActive(true);
            userRepository.save(user);
            
            log.info("Default users created successfully");
            log.info("Admin - username: admin, password: admin123");
            log.info("User - username: user, password: user123");
        }
    }
    
    private void initializeVideos() {
        if (videoRepository.count() == 0) {
            // Sample Video 1
            Video video1 = new Video();
            video1.setTitle("The Shawshank Redemption");
            video1.setDescription("Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.");
            video1.setDirector("Frank Darabont");
            video1.setGenre("Drama");
            video1.setReleaseYear(1994);
            video1.setDurationMinutes(142);
            video1.setRentalPricePerDay(new BigDecimal("3.99"));
            video1.setTotalCopies(5);
            video1.setAvailableCopies(5);
            video1.setAvailable(true);
            videoRepository.save(video1);
            
            // Sample Video 2
            Video video2 = new Video();
            video2.setTitle("The Godfather");
            video2.setDescription("The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.");
            video2.setDirector("Francis Ford Coppola");
            video2.setGenre("Crime");
            video2.setReleaseYear(1972);
            video2.setDurationMinutes(175);
            video2.setRentalPricePerDay(new BigDecimal("4.99"));
            video2.setTotalCopies(3);
            video2.setAvailableCopies(3);
            video2.setAvailable(true);
            videoRepository.save(video2);
            
            // Sample Video 3
            Video video3 = new Video();
            video3.setTitle("The Dark Knight");
            video3.setDescription("When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests.");
            video3.setDirector("Christopher Nolan");
            video3.setGenre("Action");
            video3.setReleaseYear(2008);
            video3.setDurationMinutes(152);
            video3.setRentalPricePerDay(new BigDecimal("3.99"));
            video3.setTotalCopies(4);
            video3.setAvailableCopies(4);
            video3.setAvailable(true);
            videoRepository.save(video3);
            
            // Sample Video 4
            Video video4 = new Video();
            video4.setTitle("Inception");
            video4.setDescription("A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea.");
            video4.setDirector("Christopher Nolan");
            video4.setGenre("Sci-Fi");
            video4.setReleaseYear(2010);
            video4.setDurationMinutes(148);
            video4.setRentalPricePerDay(new BigDecimal("4.49"));
            video4.setTotalCopies(6);
            video4.setAvailableCopies(6);
            video4.setAvailable(true);
            videoRepository.save(video4);
            
            // Sample Video 5
            Video video5 = new Video();
            video5.setTitle("Pulp Fiction");
            video5.setDescription("The lives of two mob hitmen, a boxer, a gangster and his wife intertwine in four tales of violence and redemption.");
            video5.setDirector("Quentin Tarantino");
            video5.setGenre("Crime");
            video5.setReleaseYear(1994);
            video5.setDurationMinutes(154);
            video5.setRentalPricePerDay(new BigDecimal("3.49"));
            video5.setTotalCopies(2);
            video5.setAvailableCopies(2);
            video5.setAvailable(true);
            videoRepository.save(video5);
            
            log.info("Sample videos created successfully");
        }
    }
}
