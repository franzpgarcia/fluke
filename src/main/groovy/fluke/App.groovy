package fluke

import de.gesellix.docker.client.AttachConfig;
import de.gesellix.docker.client.DockerClientImpl;
import fluke.cli.FlukeCli;
import fluke.docker.api.DockerApi;
import fluke.docker.api.PullProgressHandler;
import groovy.json.JsonSlurper;
import okhttp3.Response

class App {

	static void main(String[] args) {
		FlukeCli flukeCli = new FlukeCli()
		flukeCli.handle(args)
	}
}