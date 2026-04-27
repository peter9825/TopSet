package com.peter.topset.Logging.Sets.Repository;

import com.peter.topset.Logging.Sets.Entity.SetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SetRepository extends JpaRepository <SetEntity,Long> {
}
