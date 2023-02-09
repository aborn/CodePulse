// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
import * as vscode from 'vscode';
import { CodePulse } from './codepulse';
import { Logger } from './common/logger';

let codePulse: CodePulse;

// this method is called when your extension is activated
export function activate(context: vscode.ExtensionContext) {

	// Use the console to output diagnostic information (console.log) and errors (console.error)
	// This line of code will only be executed once when your extension is activated
	Logger.info('Congratulations, your extension "CodePulse" is now active!');
	codePulse = new CodePulse(context.globalState);

	// The command has been defined in the package.json file
	// Now provide the implementation of the command with registerCommand
	// The commandId parameter must match the command field in package.json
	context.subscriptions.push(
		vscode.commands.registerCommand('CodePulse.helloWorld', () => {
			vscode.window.showInformationMessage('Hello World from CodePulse plugin!');
		})
	);

	context.subscriptions.push(
		vscode.commands.registerCommand('CodePulse.config_file', () => {
			codePulse.openConfigFile();
		})
	);

	context.subscriptions.push(
		vscode.commands.registerCommand('CodePulse.token', function () {
			codePulse.promptConfig('token');
		}),
	);

	context.subscriptions.push(
		vscode.commands.registerCommand('CodePulse.id', function () {
			codePulse.promptConfig('id');
		}),
	);


	context.subscriptions.push(
		vscode.commands.registerCommand('CodePulse.level', function () {
			codePulse.promptConfig('level');
		}),
	);
}

// this method is called when your extension is deactivated
export function deactivate() {
	if (codePulse) {
		codePulse.dispose();
	}
}
