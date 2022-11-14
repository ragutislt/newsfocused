package eu.adainius.newsfocused.admin.site.back;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;

public class ArchitectureTest {

    @Test
    public void domain_package_should_not_depend_on_infra_libraries() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("eu.adainius.newsfocused.admin.site.back.domain");

        ArchRule rule = classes().that().resideInAPackage("..domain..")
                .should().onlyDependOnClassesThat().resideInAnyPackage(
                        "..lombok..",
                        "..org.apache.commons.validator..",
                        "..java.lang..",
                        "..java.util..",
                        "..io.vavr.control..",
                        "..eu.adainius.newsfocused.admin.site.back.domain..");

        rule.check(importedClasses);
    }

    @Test
    public void controller_package_should_not_depend_on_repositories() {
        JavaClasses importedClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("eu.adainius.newsfocused.admin.site.back.infrastructure.controller");

        ArchRule rule = noClasses().that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "..domain.repositories..","..infrastructure.repositories..");

        rule.check(importedClasses);
    }
}
