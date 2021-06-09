package io.agileintelligence.ppmtool.controller;

import io.agileintelligence.ppmtool.domain.Project;
import io.agileintelligence.ppmtool.services.ErrorService;
import io.agileintelligence.ppmtool.services.ProjectService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ErrorService errorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@RequestBody Project project, BindingResult result) {
        Validate.notNull(project, "Bad Request");
        if (project.getProjectName().isEmpty() || project.getProjectIdentifier().isEmpty() || project.getDescription().isEmpty()) {
            return new ResponseEntity("Project name ,Project Id or Project Description must not be null", HttpStatus.BAD_REQUEST);
        }
        ResponseEntity<?> errorMap = errorService.MapValidationService(result);
        if (errorMap != null) return errorMap;
        Project project1 = projectService.saveOrUpdate(project);
        return new ResponseEntity<>(project1, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId) {
        Project project = projectService.findProjectByIdentifier(projectId.toUpperCase());
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects() {
        return projectService.findAllProjects();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId) {
        projectService.deleteById(projectId.toUpperCase());
        return new ResponseEntity<String>("Project Deleted", HttpStatus.OK);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<?> updateProject(@RequestBody Project project ,@PathVariable String projectId){
        projectService.findProjectByIdentifier(projectId.toUpperCase());
        projectService.saveOrUpdate(project);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
