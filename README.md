# Fluke
Docker build automation tool written in groovy

# Usage
```
usage: fluke [options] <file>
Options
 -A,--build-arg <arg=value>   add build arguments
    --build <image>           build a specific image
    --help                    display this help
    --verbose                 run in verbose mode
    --version                 output version
```

# Example of build file
```groovy
procedure adddirectory: {
  with user: root {
    run shell, "mkdir", "/home/test"
  }
}
	
image custom_ubuntu: {
	from image: "ubuntu"

	maintainer = "Franz Garcia"
	labels = ["version": "0.1"]
	
	apply procedure: "adddirectory"
	apply procedure: {
		run shell, "mkdir", "/blah1"
		with directory: "/home/test", {
			run shell, "touch", "hello.text"
		}
		port 22
	}
	volume "~/bla"
	
	onstart entrypoint: "/home/test.sh", parameters: ["-d", "/home/test"]
}

build image: "custom_ubuntu"
```

# License

This project is licensed under the terms of the MIT license.
