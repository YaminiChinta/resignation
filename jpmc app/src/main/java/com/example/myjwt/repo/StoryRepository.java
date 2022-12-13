package com.example.myjwt.repo;

import com.example.myjwt.models.Epic;
import com.example.myjwt.models.Sprint;
import com.example.myjwt.models.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryRepository extends JpaRepository<Story,Long> {

    Optional<List<Story>> findByOwnerId(Long id);

    Optional<List<Story>> findBySprint(Sprint sprint);

    Optional<List<Story>> findByEpic(Epic epic);

    long countByEpic(Epic epic);

    List<Story> findByEpicAndCurrentStatus(Epic epic, String status);

    long countByEpicAndCurrentStatus(Epic epic, String status);
    
    List<Story> findByOwnerIdIn(List<Long> ownerIdList);

}