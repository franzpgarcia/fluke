package fluke.cli

import fluke.script.ScriptRunner

class FlukeCli {
	private static final String VERSION = "0.1"
	
	private CliBuilder cli
	private ScriptRunner scriptRunner
	
	FlukeCli() {
		initCli()
		this.scriptRunner = new ScriptRunner()
	}
	
	private void initCli() {
		this.cli = new CliBuilder(usage: 'fluke [options] <file>', header: 'Options')
		this.cli.A(longOpt: 'build-arg', args: 2, argName:'arg=value', valueSeparator: '=', 'add build arguments')
		this.cli.build(args: 1, argName:'<image>', 'build a specific image')
		this.cli._(longOpt: 'verbose', 'run in verbose mode')
		this.cli._(longOpt: 'help', 'display this help')
		this.cli._(longOpt: 'version', 'output version')
	}
	
	def handle(String[] args) {
		OptionAccessor option = this.cli.parse(args)
		if(option.help) {
			this.help()
		} else if(option.version) {
			this.version()
		} else {
			if(option.arguments().size() != 1) {
				//TODO error
				this.help()
			} else {
				this.runScript(option.arguments()[0], option.build?:null, (option.As?:[]).collate(2, 2).collectEntries())
			}
		}
	}
	
	private void runScript(String path, String build, Map args) {
		File file = new File(path)
		FileInputStream stream = new FileInputStream(file)
		String script = stream.readLines().join("\n")
		this.scriptRunner.runScript(script, build, args)
	}
	
	private void help() {
		this.cli.usage()
	}
	
	private void version() {
		println "Version ${VERSION}"
	}
}
