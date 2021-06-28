#load the required packages
load.packages <- function(packages) {
  # start loop to determine if each package is installed
  for(package.name in names(packages)){
    # if package is installed locally, load
    if(package.name %in% rownames(installed.packages()))
      do.call('library', list(package.name))
    
    # if package is not installed locally, download, then load
    else {
      install.packages(packages[package.name], repos = NULL, type = "win.binary")
      do.call("library", list(package.name))
    }
  }
}