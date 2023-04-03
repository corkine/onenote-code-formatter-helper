import 'dart:async';

import 'package:clipboard/clipboard.dart';
import 'package:flutter/material.dart';
import 'package:onenote_code_formatter/tray.dart';

var convertToData = "";

var convertSC = StreamController<String>();

void readClipBoardAndConvert() async {
  var data = await FlutterClipboard.paste();
  print("data is $data");
  convertToData = data.toString();
  convertSC.sink.add(convertToData);
  FlutterClipboard.copy(convertToData);
  print("paste done!");
}

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  initSystemTray(readClipBoardAndConvert);
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'OneNote Code Converter',
      theme: ThemeData(
        useMaterial3: true,
        colorSchemeSeed: Colors.pink,
      ),
      home: const MyHomePage(title: 'OneNote Code Converter'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});
  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text(widget.title),
        ),
        body: StreamBuilder<String>(
          stream: convertSC.stream,
          builder: (context, snap) {
            if (snap.hasData) {
              return SingleChildScrollView(
                child: Center(
                  child: Text(snap.data!),
                ),
              );
            }
            return const Center(
              child: CircularProgressIndicator(),
            );
          },
        ) // This trailing comma makes auto-formatting nicer for build methods.
        );
  }
}
