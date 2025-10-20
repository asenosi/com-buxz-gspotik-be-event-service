package com.buxz.gspotik.events;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;
import static com.tngtech.archunit.library.GeneralCodingRules.BE_ANNOTATED_WITH_AN_INJECTION_ANNOTATION;
import static com.tngtech.archunit.library.freeze.FreezingArchRule.freeze;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.freeze.FreezingArchRule;

@AnalyzeClasses(packages = "com.buxz.gspotik.events")
@SuppressWarnings("unused")
public class CodingGuidelinesTest {

    @ArchTest
    private static final FreezingArchRule no_google = freeze(
        noClasses()
            .should().dependOnClassesThat().resideInAPackage("..google.[common|guava]..")
            .as("project should not use unnecessary Google libraries"));

    @ArchTest
    private static final ArchRule no_mutiny = noClasses()
        .should().dependOnClassesThat().resideInAnyPackage("io.smallrye.mutiny..")
        .as("Codebase should not rely on Mutiny features")
        .because("Rather use industry standards like Eclipse MicroProfile + CompletionStage where possible. "
            + "Mutiny is a great library, but it is not a standard and might be replaced in the future.");

    @ArchTest
    private final ArchRule no_field_injection =
        noFields()
            .that().areDeclaredInClassesThat().haveNameNotMatching(".*[Test|Impl]") // field injection in tests is acceptable
            .and().haveNameNotMatching("log.*") // field injection for logger instances is acceptable
            .should(BE_ANNOTATED_WITH_AN_INJECTION_ANNOTATION)
            .as("no classes should use field injection")
            .because("field injection is considered harmful; use constructor injection or setter injection instead; "
                + "see https://stackoverflow.com/q/39890849 for detailed explanations");


}
