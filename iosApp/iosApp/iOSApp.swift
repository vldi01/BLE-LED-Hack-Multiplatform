import SwiftUI

@main
struct iOSApp: App {
    init() {
        KoinKt.initKoin()
    }

	var body: some Scene {
		WindowGroup {
		    ZStack {
		        Color.white.ignoresSafeArea(.all) // status bar color
			    ContentView()
			}.preferredColorScheme(.light)
		}
	}
}