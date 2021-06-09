package io.agileintelligence.ppmtool.services;

import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.exceptions.ProjectIdException;
import io.agileintelligence.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdate(Project project){
        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project ID'"+ project.getProjectIdentifier().toUpperCase()+"'already exists!");
        }
    }

    public Project findProjectByIdentifier(String projectId){
        Project project= projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project == null){
            throw new ProjectIdException("Project Id doesn't exists");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteById(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if (project == null){
            throw new ProjectIdException("Cannot Delete project as project with Id doesn't exists");
        }

        projectRepository.delete(project);
    }

}
