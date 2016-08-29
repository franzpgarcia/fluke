# Fluke
Docker build automation tool written in groovy

# Example of build file
```
procedure adddirectory: {
  with user: root {
    run shell, "mkdir", "/home/test"
  }
}
	
image custom_ubuntu: {
	maintainer = "Franz Garcia"
	labels = ["version": "0.1"]
	onstart entrypoint: "/home/test.sh", parameters: ["-d", "/home/test"]
	from image: "ubuntu"

	apply procedure: "adddirectory"
	apply procedure: {
		run shell, "mkdir", "/blah1"
		with directory: "/home/test", {
			run shell, "touch", "hello.text"
		}
		port 22
	}
	volume "~/bla"
}

build image: "custom_ubuntu"
```
