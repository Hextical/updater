# updater

Currently this can be used to detect updates from a given `mods` folder. Only works with CurseForge mods right now.

## Usage

One can utilize the program by locating the path to the `mods` folder and specifying
the `game version`. Check out the [releases](https://github.com/Hextical/updater/releases) tab for a jar compiled.

Example command-line usage:
`
java -jar updater-0.0.1.jar "C:\Users\hexii\Documents\MultiMC\instances\1.12.2\.minecraft\mods" 1.12.2
`
A file `log.all` will be placed in the directory of execution. Right now there's not really any functionality for the manifests generated in the `log.all` file.

## Building
Clone the repository, and run the `build.bat` file, very simple.