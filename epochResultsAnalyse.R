
library(tidyverse)

# Set current directory to parent directory
setwd(dirname(rstudioapi::getSourceEditorContext()$path))


for (i in c(1:100))
{
    # Read main csv file
    epoch_Results_DataFrame <- read.csv(paste("results", .Platform$file.sep, "EpochResultForNeuralNetwork-63-", i, "-7.csv", sep = ""))
    
    # Plot
    plot(ggplot(data = epoch_Results_DataFrame, aes(c(1:nrow(epoch_Results_DataFrame)), epoch_Results_DataFrame[, 1])) + 
             xlab("Época") + 
             ylab("Erro quadrático médio") +
             geom_point(size = 1, color = "red") + 
             geom_point(y = epoch_Results_DataFrame[, 2], size = 1, color = "blue") + 
             ggtitle(paste("Neural Network 63-", i, "-7", sep = "")) + 
             scale_x_continuous(n.breaks = 12) + 
             scale_y_continuous(n.breaks = 15))
}

