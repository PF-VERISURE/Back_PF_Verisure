package com.verisure.backend.repository.projection;

import com.verisure.backend.entity.Project;

public interface ProjectAdminProjection {
    
    Project getProject();
    Long getFavCount();
    Long getAppCount();

}