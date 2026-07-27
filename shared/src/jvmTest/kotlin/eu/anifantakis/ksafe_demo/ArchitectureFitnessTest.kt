package eu.anifantakis.ksafe_demo

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ArchitectureFitnessTest {
    private val featureRoot: Path by lazy {
        val workingDirectory = Path.of(System.getProperty("user.dir"))
        listOf(
            workingDirectory.resolve("shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features"),
            workingDirectory.resolve("src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features"),
        ).firstOrNull(Files::isDirectory)
            ?: error("Cannot locate the features package from $workingDirectory")
    }

    private val featureNames = listOf("storage", "flows", "custom_json", "security", "preferences")

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
    fun storageRetainsItsOriginalCenteredScrollableLayout() {
        val source = Files.readString(
            featureRoot.resolve("storage/presentation/screens/storage/StorageScreen.kt"),
        )

        assertTrue(source.contains("contentAlignment = Alignment.Center"))
        assertTrue(source.contains(".wrapContentHeight()"))
        assertTrue(source.indexOf("Box(") < source.indexOf("Column("))
        assertTrue(
            Regex(
                """private fun ValueCard[\s\S]*?Column\(\s*modifier = Modifier\s*""" +
                    """\.fillMaxWidth\(\)[\s\S]*?horizontalAlignment = Alignment\.CenterHorizontally""",
            ).containsMatchIn(source),
            "Storage ValueCard content must occupy the card width before centering its children",
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
}
