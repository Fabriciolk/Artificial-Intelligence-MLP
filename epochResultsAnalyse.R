
library(tidyverse)

# Set current directory to parent directory
setwd(dirname(rstudioapi::getSourceEditorContext()$path))
# Get all files in 'results' folder
folderName <- "results"
fileName_list <- list.files(path = paste(folderName, .Platform$file.sep, sep = ""), pattern = ".csv")


for (fileName in fileName_list)
{
    epoch_Results_DataFrame <- read.csv(paste(folderName, .Platform$file.sep, fileName, sep = ""))
    
    # Plot
    plot(ggplot(data = epoch_Results_DataFrame, aes(c(1:nrow(epoch_Results_DataFrame)), epoch_Results_DataFrame[, 1])) + 
             xlab("Época") + 
             ylab("Erro quadrático médio") +
             geom_point(size = 1, color = "red") + 
             geom_line() +
             geom_point(y = epoch_Results_DataFrame[, 2], size = 1, color = "blue") + 
             ggtitle(fileName) + 
             scale_x_continuous(n.breaks = 12) + 
             scale_y_continuous(n.breaks = 15))
}

