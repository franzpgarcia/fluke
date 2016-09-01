package fluke

import fluke.api.DockerApi;
import fluke.cli.FlukeCli;

class App {

	static void main(String[] args) {
		FlukeCli flukeCli = new FlukeCli()
		flukeCli.handle(args)
	}
}
