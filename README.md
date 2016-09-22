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
proc adddirectory(path:String) {
  with user: root {
      shell "mkdir", path
  }
}
	
image custom_ubuntu {
	from "ubuntu"

	maintainer = "Franz Garcia"
	labels = ["version": "0.1"]
	
	apply adddirectory("/home/test")
	
	shell "ls", "-la"
	with directory: "/home/test", {
		shell "touch", "hello.text"
	}
	port 22
	volume "~/bla"
	
	onstart "/home/test.sh", "-d", "/home/test"
}

build custom_ubuntu
```

# License

This project is licensed under the terms of the MIT license.
