# Check if should install packages
list.of.packages <- c("tidyverse", "gt")
new.packages <- list.of.packages[!(list.of.packages %in% installed.packages()[,"Package"])]
if(length(new.packages)) install.packages(new.packages)

library(tidyverse)
library(gt)

# Set current directory to parent directory
#setwd(dirname(rstudioapi::getSourceEditorContext()$path))

# Get all files in 'results' and 'results/table' folders
folderName <- "results"
folderTableName <- "table"
fileName_list_forPlot <- list.files(path = paste(folderName, .Platform$file.sep, sep = ""), pattern = ".csv")
fileName_list_forTable <- list.files(path = paste(folderName, .Platform$file.sep, folderTableName, .Platform$file.sep,sep = ""), pattern = ".csv")

pdf(NULL)

for (fileName in fileName_list_forPlot)
{
    current_dataframe <- read.csv(paste(folderName, .Platform$file.sep, fileName, sep = ""))

    # Plot
    plot(ggplot(data = current_dataframe, aes(c(1:nrow(current_dataframe)), current_dataframe[, 1])) +
             xlab("Época") +
             ylab("Erro quadrático médio") +
             geom_point(size = 1, color = "red") +
             geom_line() +
             geom_point(y = current_dataframe[, 2], size = 1, color = "blue") +
             ggtitle(fileName) +
             scale_x_continuous(n.breaks = 12) +
             scale_y_continuous(n.breaks = 15))
    
    ggsave(paste("images", .Platform$file.sep, str_replace(fileName, ".csv", ".png"), sep = ""))
}


for (fileName in fileName_list_forTable)
{
    current_dataframe <- read.csv(paste(folderName, .Platform$file.sep, folderTableName, .Platform$file.sep, fileName, sep = ""))
    colnames(current_dataframe)[1] <- " "

    title <- NULL
    subtitle <- NULL
    
    if (current_dataframe[1, 1] == "Bias")
    {
        title <- "Weights and Bias"
        subtitle <- paste("Synapse ", strsplit(strsplit(fileName, split = "_")[[1]][2], split = ".csv")[[1]][1], sep = "")
    }
    else if (grepl("Should_Be", current_dataframe[1, 1], fixed = TRUE))
    {
        title <- "Confusion Matrix"
        subtitle <- " "
    }
    else
    {
        title <- ""
        subtitle <- ""
    }
    
    max_image_size <- (nrow(current_dataframe) + 1)*100
    
    print(gt(current_dataframe) %>% 
              tab_header(
                  title = md(title),
                  subtitle = md(subtitle)
              ) %>%
              gtsave(paste("images", .Platform$file.sep, str_replace(fileName, ".csv", ".png"), sep = ""), 
                     vwidth = max_image_size, 
                     vheight = max_image_size))
}

