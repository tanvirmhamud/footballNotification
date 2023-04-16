import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:footballnotification/footballnotification.dart';
import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _footballnotificationPlugin = Footballnotification();

  Future permissioncheck() async {
    final status = await Permission.notification.status;
    if (status.isDenied) {
      await Permission.notification.request();
    } else {
      print(status.toString());
    }
  }

  @override
  void initState() {
    super.initState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await _footballnotificationPlugin.getPlatformVersion([980893, 980894, 981258, 995005, 995421, 995422]) ??
              'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            Center(
              child: MaterialButton(
                color: Colors.indigo,
                onPressed: () {
                  initPlatformState();
                },
                child: Text("background"),
              ),
            ),
            MaterialButton(
              color: Colors.indigo,
              onPressed: () {
                permissioncheck();
              },
              child: Text("Notification Permission"),
            )
          ],
        ),
      ),
    );
  }
}
