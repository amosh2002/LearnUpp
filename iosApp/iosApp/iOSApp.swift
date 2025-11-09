import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    // Tell SwiftUI that your AppDelegate is the application delegate
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    init() {
        Koin_iosKt.doInitKoinIos()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
