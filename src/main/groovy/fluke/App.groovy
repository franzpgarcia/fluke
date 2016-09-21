package fluke

import de.gesellix.docker.client.AttachConfig;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.api.DockerApi;
import fluke.api.PullProgressHandler;
import fluke.cli.FlukeCli;
import groovy.json.JsonSlurper;
import okhttp3.Response

class App {

	static void main(String[] args) {
		FlukeCli flukeCli = new FlukeCli()
		//flukeCli.handle(args)
		flukeCli.handle(["-f", "C:\\Users\\Franz\\Desktop\\test.fluke"] as String[])
	}
}