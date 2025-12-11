package ru.hogwarts.school.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class AvatarService {

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        try {
            logger.info("Was invoked method uploading avatar image for student with id = {}", studentId);
            Student student = studentRepository.findById(studentId).orElseThrow(() -> new EntityNotFoundException("There is not student with id = " + studentId));
            Path filePath = Path.of(avatarsDir, student + "." + getExtension(Objects.requireNonNull(avatarFile.getOriginalFilename())));
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);
            try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
            ) {
                bis.transferTo(bos);
            }
            Avatar avatar = findAvatar(studentId);
            avatar.setStudent(student);
            avatar.setFilePath(filePath.toString());
            avatar.setFileSize(avatarFile.getSize());
            if (avatarFile.getSize() == 0) logger.warn("Size of file avatar is zero");
            avatar.setMediaType(avatarFile.getContentType());
            byte[] myByteArray = avatarFile.getBytes();
            avatar.setData(myByteArray);
            avatarRepository.save(avatar);
        } catch (Exception e) {
            logger.debug("Are you delete this student, or he is never exist? Or maybe unforeseen exception.");
            logger.error(e.getMessage(), e);
        }
    }

    public Avatar findAvatar(Long studentId) {
        logger.info("Was invoked method searching avatar image for student with id = {}", studentId);
        return avatarRepository.findByStudentId(studentId).orElse(new Avatar());
    }

    private String getExtension(String originalFilename) {
        return originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }

    public List<Avatar> getAllAvatars(Integer pageNumber, Integer pageSize) {
        logger.info("Was invoked method for get avatars list by pages");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
