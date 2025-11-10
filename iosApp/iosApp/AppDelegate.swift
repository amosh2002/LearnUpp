//
//  AppDelegate.swift
//  iosApp
//
//  Created by Armen Armenakyan on 27.01.25.
//  Copyright © 2025 orgName. All rights reserved.
//

import UIKit
import UserNotifications
import ComposeApp
import os.log
import AVFoundation

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    ) -> Bool {
        // Add notification setting later here
        // Configure audio session for video playback (works in silent mode)
        do {
            try AVAudioSession.sharedInstance().setCategory(.playback, mode: .moviePlayback, options: [])
            try AVAudioSession.sharedInstance().setActive(true)
        } catch {
            os_log("Failed to configure AVAudioSession: %@", type: .error, error.localizedDescription)
        }

        return true
    }



}
