import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`

plugins {
    `maven-publish`
    signing
}

publishing {
    // Configure all publications
    publications.withType<MavenPublication> {
        // Stub javadoc.jar artifact
        artifact(tasks.register("${name}JavadocJar", Jar::class) {
            archiveClassifier.set("javadoc")
            archiveAppendix.set(this@withType.name)
        })

        // Provide artifacts information required by Maven Central
        pom {
            name.set("kLocation")
            description.set("A kotlin multiplatform library for getting a device's location")
            url.set("https://github.com/addhen/k-location")

            licenses {
                license {
                    name.set("Apache 2.0")
                    url.set("https://opensource.org/license/apache-2-0")
                }
            }
            developers {
                developer {
                    id.set("Addhen Ltd")
                    name.set("Addhen Ltd Team")
                    organization.set("Addhen Ltd")
                    organizationUrl.set("https://www.jetbrains.com")
                }
            }
            scm {
                url.set("https://github.com/addhen/k-location")
            }
        }
    }
}

signing {
    if (project.hasProperty("signing.gnupg.keyName")) {
        useGpgCmd()
        sign(publishing.publications)
    }
}
