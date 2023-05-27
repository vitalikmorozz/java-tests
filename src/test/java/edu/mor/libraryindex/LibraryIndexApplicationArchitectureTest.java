package edu.mor.libraryindex;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class LibraryIndexApplicationArchitectureTest {

    private JavaClasses importedClasses;

    @BeforeEach
    void setUp() {
        importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_ARCHIVES)
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_PACKAGE_INFOS)
            .importPackages("edu.mor.libraryindex");
    }

    @Test
    void shouldFollowLayersArchitecture() {
        layeredArchitecture()
            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Repository").definedBy("..repository..")
            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller", "Service")
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
            .check(importedClasses);
    }

    @Test
    void servicesShouldNotDependOnControllerLevel() {
        noClasses()
            .that().resideInAPackage("..service..")
            .should()
            .dependOnClassesThat()
            .resideInAPackage("..controller..")
            .because("out of rules")
            .check(importedClasses);
    }

    @Test
    void controllerClassesShouldBeNamedXController() {
        classes()
            .that().resideInAPackage("..controller..")
            .should()
            .haveSimpleNameEndingWith("Controller")
            .check(importedClasses);

    }

    @Test
    void serviceClassesShouldBeNamedXService() {
        classes()
            .that().resideInAPackage("..service..")
            .should()
            .haveSimpleNameEndingWith("Service")
            .check(importedClasses);

    }

    @Test
    void repositoryClassesShouldBeNamedXRepository() {
        classes()
            .that().resideInAPackage("..repository..")
            .should()
            .haveSimpleNameEndingWith("Repository")
            .check(importedClasses);

    }

    @Test
    void controllerClassesShouldBeAnnotatedByRestController() {
        classes()
            .that().resideInAPackage("..controller..")
            .should()
            .beAnnotatedWith(RestController.class)
            .orShould()
            .beAnnotatedWith(Controller.class)
            .andShould()
            .beAnnotatedWith(RequestMapping.class)
            .check(importedClasses);
    }

    @Test
    void servicesClassesShouldBeAnnotatedByService() {
        classes()
            .that().resideInAPackage("..service..")
            .should()
            .beAnnotatedWith(Service.class)
            .check(importedClasses);
    }

    @Test
    void repositoryInterfacesShouldBeAnnotatedByRepository() {
        classes()
            .that().resideInAPackage("..repository..")
            .should()
            .beAnnotatedWith(Repository.class)
            .check(importedClasses);
    }

    @Test
    void modelsClassesShouldBeAnnotatedByDocument() {
        classes()
            .that().resideInAPackage("..model..")
            .should()
            .beAnnotatedWith(Document.class)
            .check(importedClasses);
    }

    @Test
    void repositoriesShouldBeInterfaces() {
        classes()
            .that().resideInAPackage("..repository..")
            .should()
            .beInterfaces()
            .check(importedClasses);
    }

    @Test
    void shouldNotUseAutowiredFields() {
        noFields()
            .should().beAnnotatedWith(Autowired.class)
            .check(importedClasses);
    }
}