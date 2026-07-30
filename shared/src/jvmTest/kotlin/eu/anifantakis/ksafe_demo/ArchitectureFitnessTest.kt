package eu.anifantakis.ksafe_demo

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArchitectureFitnessTest {
    private val sharedSourceSetsRoot: Path by lazy {
        val workingDirectory = Path.of(System.getProperty("user.dir"))
        listOf(
            workingDirectory.resolve("shared/src"),
            workingDirectory.resolve("src"),
        ).firstOrNull(Files::isDirectory)
            ?: error("Cannot locate shared source sets from $workingDirectory")
    }

    private val featureRoot: Path by lazy {
        val workingDirectory = Path.of(System.getProperty("user.dir"))
        listOf(
            workingDirectory.resolve("shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features"),
            workingDirectory.resolve("src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features"),
        ).firstOrNull(Files::isDirectory)
            ?: error("Cannot locate the features package from $workingDirectory")
    }

    private val featureNames =
        listOf("counters", "flows", "custom_json", "security", "preferences", "about")

    @Test
    fun everyAppFeatureLivesUnderTheFeaturesPackage() {
        featureNames.forEach { feature ->
            assertTrue(
                Files.isDirectory(featureRoot.resolve(feature)),
                "Feature '$feature' must live under $featureRoot",
            )
        }
    }

    @Test
    fun sharedSourceSetsDoNotExposeAnOwnerlessTopLevelUtilPackage() {
        val ownerlessUtilPackages = Files.walk(sharedSourceSetsRoot).use { paths ->
            paths
                .filter(Files::isDirectory)
                .filter { path ->
                    path.toString()
                        .replace('\\', '/')
                        .endsWith("/kotlin/eu/anifantakis/ksafe_demo/util")
                }
                .iterator()
                .asSequence()
                .toList()
        }

        assertTrue(
            ownerlessUtilPackages.isEmpty(),
            "Move each top-level util to its owning feature or a semantic core package: " +
                ownerlessUtilPackages,
        )
    }

    @Test
    fun platformSeamsLiveWithTheirOwners() {
        assertTrue(
            Files.isRegularFile(
                featureRoot.resolve(
                    "counters/presentation/platform/LockTestExecutionWindow.kt",
                ),
            ),
        )
        assertTrue(
            Files.isRegularFile(
                featureRoot.parent.resolve("core/data/persistence/KSafeStartup.kt"),
            ),
        )
    }

    @Test
    fun countersRetainsItsOriginalCenteredScrollableLayout() {
        val screenSource = Files.readString(
            featureRoot.resolve("counters/presentation/screens/counters/CountersScreen.kt"),
        )
        val componentSource = Files.readString(
            coreComponentsRoot.resolve("content/AppValueCard.kt"),
        )

        assertTrue(screenSource.contains("contentAlignment = Alignment.Center"))
        assertTrue(screenSource.contains(".wrapContentHeight()"))
        assertTrue(screenSource.indexOf("Box(") < screenSource.indexOf("Column("))
        assertTrue(screenSource.contains("AppValueCard("))
        assertTrue(
            Regex(
                """fun AppValueCard[\s\S]*?Column\(\s*modifier = Modifier\s*""" +
                    """\.fillMaxWidth\(\)[\s\S]*?horizontalAlignment = Alignment\.CenterHorizontally""",
            ).containsMatchIn(componentSource),
            "AppValueCard content must occupy the card width before centering its children",
        )
    }

    @Test
    fun featureScreensExposeRootAndKeepStatelessScreenPrivate() {
        featureFiles("Screen.kt").forEach { screenFile ->
            val source = Files.readString(screenFile)
            val screenName = screenFile.name.removeSuffix(".kt")

            assertTrue(
                source.contains("fun ${screenName}Root("),
                "$screenFile must expose ${screenName}Root",
            )
            assertTrue(
                source.contains("private fun $screenName("),
                "$screenFile must keep the stateless $screenName private",
            )
        }
    }

    @Test
    fun featureScreensUseTheDesignSystemFacade() {
        featureFiles("Screen.kt").forEach { screenFile ->
            val source = Files.readString(screenFile)
            assertFalse(
                source.contains("import androidx.compose.material3."),
                "$screenFile bypasses the App* design-system façade",
            )
        }
    }

    @Test
    fun featureScreenFilesKeepCustomComponentsInTheCoreDesignSystem() {
        featureFiles("Screen.kt").forEach { screenFile ->
            val source = Files.readString(screenFile)
            val screenName = screenFile.name.removeSuffix(".kt")
            val privateComposableNames =
                Regex("""@Composable\s+private fun\s+([A-Z][A-Za-z0-9_]*)\(""")
                .findAll(source)
                .map { match -> match.groupValues[1] }
                .toList()

            privateComposableNames.forEach { name ->
                assertTrue(
                    name == screenName || "Preview" in name,
                    "$screenFile declares custom composable '$name'; move it to " +
                        "core/presentation/design_system/components with an App* name",
                )
            }
        }
    }

    @Test
    fun exportedDesignSystemComposablesUseTheAppPrefix() {
        coreComponentFiles().forEach { componentFile ->
            val source = Files.readString(componentFile)
            val exportedNames = exportedComposableNames(source)

            exportedNames.forEach { name ->
                assertTrue(
                    name.startsWith("App"),
                    "$componentFile exports custom composable '$name' without the App prefix",
                )
            }
        }
    }

    @Test
    fun designSystemComponentsLiveOnePerFile() {
        coreComponentFiles().forEach { componentFile ->
            val exportedNames = exportedComposableNames(Files.readString(componentFile)).distinct()

            assertTrue(
                exportedNames.size <= 1,
                "$componentFile exports multiple custom composables: $exportedNames",
            )
            exportedNames.singleOrNull()?.let { componentName ->
                assertTrue(
                    componentFile.name == "$componentName.kt",
                    "$componentFile must be named $componentName.kt",
                )
            }
        }
    }

    @Test
    fun everyDesignSystemComponentOwnsItsLightDarkPreview() {
        coreComponentFiles().forEach { componentFile ->
            val source = Files.readString(componentFile)
            val componentName = exportedComposableNames(source).distinct().singleOrNull()
                ?: return@forEach

            assertTrue(
                source.contains("@PreviewLightDark"),
                "$componentFile must preview $componentName in light and dark themes",
            )
            assertTrue(
                Regex("""@PreviewLightDark\s+@Composable\s+private fun Preview$componentName\(""")
                    .containsMatchIn(source),
                "$componentFile must keep Preview$componentName beside the component",
            )
        }
    }

    @Test
    fun featureViewModelsUseOneIntentEntryPoint() {
        featureFiles("ViewModel.kt").forEach { viewModelFile ->
            val source = Files.readString(viewModelFile)
            assertTrue(
                source.contains(": BaseGlobalViewModel()"),
                "$viewModelFile must extend BaseGlobalViewModel",
            )
            assertTrue(
                source.contains("fun onAction("),
                "$viewModelFile must expose onAction(intent)",
            )
        }
    }

    private fun featureFiles(suffix: String): List<Path> = featureNames.flatMap { feature ->
        val root = featureRoot.resolve(feature)
        Files.walk(root).use { paths ->
            paths
                .filter { Files.isRegularFile(it) && it.extension == "kt" && it.name.endsWith(suffix) }
                .iterator()
                .asSequence()
                .toList()
        }
    }

    private val coreComponentsRoot: Path
        get() = featureRoot.parent.resolve("core/presentation/design_system/components")

    private fun coreComponentFiles(): List<Path> =
        Files.walk(coreComponentsRoot).use { paths ->
            paths
                .filter { Files.isRegularFile(it) && it.extension == "kt" }
                .iterator()
                .asSequence()
                .toList()
        }

    private fun exportedComposableNames(source: String): List<String> =
        Regex("""@Composable\s+(?:internal\s+|public\s+)?fun\s+([A-Z][A-Za-z0-9_]*)\(""")
            .findAll(source)
            .map { it.groupValues[1] }
            .toList()
}
