import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    let splashMode: AppSplashMode
    let minimumSplashDurationMillis: Int64
    let onPlatformSplashReadyToDismiss: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.ConfiguredMainViewController(
            splashMode: splashMode,
            minimumSplashDurationMillis: minimumSplashDurationMillis,
            onPlatformSplashReadyToDismiss: {
                onPlatformSplashReadyToDismiss()
            }
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    @State private var isPlatformSplashVisible = true

    private let splashMode = AppSplashMode.custom
    private let minimumSplashDurationMillis: Int64 = 0

    var body: some View {
        ZStack {
            ComposeView(
                splashMode: splashMode,
                minimumSplashDurationMillis: minimumSplashDurationMillis,
                onPlatformSplashReadyToDismiss: {
                    DispatchQueue.main.async {
                        isPlatformSplashVisible = false
                    }
                }
            )
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler

            if isPlatformSplashVisible {
                NativeSplashOverlay()
            }
        }
    }
}

private struct NativeSplashOverlay: View {
    var body: some View {
        ZStack {
            Color("LaunchBackground")
                .ignoresSafeArea()

            Image("LaunchIcon")
                .resizable()
                .scaledToFit()
                .frame(width: 112, height: 112)
        }
    }
}
