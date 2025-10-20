package com.buxz.gspotik.events;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideOutsideOfPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.freeze.FreezingArchRule;

@AnalyzeClasses(packages = "com.buxz.gspotik.events")
@SuppressWarnings("unused")
public class ArchitecturalTest {

    @ArchTest
    private static final ArchRule domain_should_not_access_adapters = noClasses()
        .that().resideInAPackage("..domain..")
        .and().haveNameNotMatching(".*(Test|Scenario).*")
        .should().dependOnClassesThat().resideInAnyPackage("..adapter..")
        .as("The domain should be self-contained and not depend on adapters");

    @ArchTest
    private static final ArchRule domain_should_throw_business_exceptions = methods()
        .that().areDeclaredInClassesThat().resideInAPackage("..domain..")
        .should().notDeclareThrowableOfType(resideOutsideOfPackage("(..domain.exception..|java.lang..)"))
        .as("If a domain method throws an exception, it should be an exception related to be business logic");

    @ArchTest
    public static final ArchRule adapters_should_not_depend_on_each_other = slices()
        .matching("..adapter.(*)..")
        .should().notDependOnEachOther()
        .as("An adapter may only depend on the domain, but not on other adapters");

    @ArchTest
    private static final ArchRule outbound_adapters_implement_outbound_ports =
        classes()
            .that().resideInAPackage("..adapter.outbound.*")
            .and().areNotAnonymousClasses()
            .and().haveNameNotMatching(".*(Test|Scenario).*")
            .should().implement(resideInAPackage("..domain.port.outbound.."))
            .as("Outbound adapters must implement outbound ports");

    @ArchTest
    private static final FreezingArchRule domain_services_implement_inbound_ports = FreezingArchRule.freeze(
        classes()
            .that().resideInAPackage("..domain.service")
            .and().areNotAnonymousClasses()
            .and().haveNameNotMatching(".*(Test|Scenario).*")
            .should().implement(resideInAPackage("..domain.port.inbound.."))
            .as("The domain must implement inbound ports"));
}
