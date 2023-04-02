import 'dart:io';
import 'package:flutter/material.dart';
import 'package:system_tray/system_tray.dart';

Future<void> initSystemTray(Function handlePaste) async {
  String path =
      Platform.isWindows ? 'assets/app_icon.ico' : 'assets/app_icon.png';

  final AppWindow appWindow = AppWindow();
  final SystemTray systemTray = SystemTray();

  // We first init the systray menu
  await systemTray.initSystemTray(iconPath: path, toolTip: "右键点击复制剪贴板代码并转换");

  // create context menu
  final Menu menu = Menu();
  await menu.buildFrom([
    MenuItemLabel(label: '显示', onClicked: (menuItem) => appWindow.show()),
    MenuItemLabel(label: '隐藏', onClicked: (menuItem) => appWindow.hide()),
    MenuItemLabel(label: '退出', onClicked: (menuItem) => appWindow.close()),
  ]);

  // set context menu
  await systemTray.setContextMenu(menu);

  // handle system tray event
  systemTray.registerSystemTrayEventHandler((eventName) {
    debugPrint("eventName: $eventName");
    if (eventName == kSystemTrayEventRightClick) {
      handlePaste();
    } else if (eventName == kSystemTrayEventClick) {
      Platform.isWindows ? appWindow.show() : systemTray.popUpContextMenu();
    } else if (eventName == kSystemTrayEventRightClick) {
      Platform.isWindows ? systemTray.popUpContextMenu() : appWindow.show();
    }
  });
}
