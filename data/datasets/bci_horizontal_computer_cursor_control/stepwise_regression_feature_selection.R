library(R.matlab)#for reading mat file from MATLAB
source("r_fcs/stepwise.R")

#--FIRST ENTER FEATURE SELECTION FEATURES CHAIN--#
feature_selected <- "u(4,3) u(2,2)"
file_out_name<-"stepwise_run2.txt"

pathname <- file.path(paste(getwd(),"/db/RTransfer/FS_Dataset.mat",sep=""))
FS_Dataset <-readMat(pathname);#read matfile
FS_Dataset <- as.array(FS_Dataset[["FS.Dataset"]])

NVar <- dim(FS_Dataset)[2]
N <- dim(FS_Dataset)[1]

lm_model0 <-""
lm_model0<-paste(lm_model0,"FS_Dataset[,",sep="")
lm_model0<-paste(lm_model0,NVar,sep="")
lm_model0<-paste(lm_model0,"]~",sep="")
lm_model0<-paste(lm_model0,"FS_Dataset[,",sep="")
lm_model0<-paste(lm_model0,1,sep="")
lm_model0<-paste(lm_model0,"]",sep="")

lm_model <-""
lm_model<-paste(lm_model,"FS_Dataset[,",sep="")
lm_model<-paste(lm_model,NVar,sep="")
lm_model<-paste(lm_model,"]~",sep="")
for(i in 1:(NVar-2)){
	lm_model<-paste(lm_model,"FS_Dataset[,",sep="")
	lm_model<-paste(lm_model,i,sep="")
	lm_model<-paste(lm_model,"]",sep="")
	lm_model<-paste(lm_model,"+",sep="")
}
lm_model<-paste(lm_model,"FS_Dataset[,",sep="")
lm_model<-paste(lm_model,NVar-1,sep="")
lm_model<-paste(lm_model,"]",sep="")

alpha_e <- 0.01
alpha_r <- 0.05

cur_vars <- stepwise(lm_model,lm_model0,alpha_e,alpha_r)

#get variables ids
n <- length(cur_vars)

if(n > 0){
feature_selector<-""
ids <- rep(0,n)
for(i in 1:n){
 L <- nchar(cur_vars[i])
 ids[i] <- strtoi(substring(cur_vars[i],14,L-1))
}
(ids)

Ni <- length(ids)

for(i in 1:Ni){
  feature_selector<-paste(feature_selector,toString(ids[[i]]))
}

features_selected_list <- unlist(strsplit(feature_selected,"[ ]"))
features_out = features_selected_list[ids]
n <- length(features_out)

features<-""
for(i in 1:n){
  features<-paste(features,features_out[i])
}
features
file_out <- paste("db/RTransfer/",file_out_name,sep="")
file_out_selector <- paste("db/RTransfer/selector_",file_out_name,sep="")
write(features,file=file_out)
write(feature_selector,file=file_out_selector)
}else{
 print("No Feature in the Model")
}