package com.example.sorting.repository;

import com.example.sorting.entity.UserDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDto, Integer> {
    Optional<UserDto> findByMatricAndPassword(Long matric, String password);
    Optional<UserDto> findFirstByMatric(Long matric);




    @Query("SELECT u FROM UserDto u WHERE "
            + "(:department IS NULL OR :department = '' OR u.department = :department) AND "
            + "(:faculty IS NULL OR :faculty = '' OR u.faculty = :faculty) AND "
            + "(:gender IS NULL OR :gender = '' OR u.gender = :gender) AND "
            + "(:mode IS NULL OR :mode = '' OR u.modeOfEntry = :mode) AND "
            + "(:year IS NULL OR u.yearOfEntry = :year) AND "
            + "(:level IS NULL OR u.level = :level) AND "
            + "(:state IS NULL OR :state = '' OR u.stateOfOrigin = :state) AND "
            + "(:minAge IS NULL OR u.age >= :minAge) AND "
            + "(:maxAge IS NULL OR u.age <= :maxAge)")
    List<UserDto> findByFilters(@Param("department") String department,
                                @Param("faculty") String faculty,
                                @Param("gender") String gender,
                                @Param("mode") String mode,
                                @Param("year") Integer year,
                                @Param("level") Integer level,
                                @Param("state") String state,
                                @Param("minAge") Integer minAge,
                                @Param("maxAge") Integer maxAge);




}