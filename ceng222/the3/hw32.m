N = 38416;
lambda = 20;
TotalNumOfMinions = zeros(N, 1);

a = 0; b = 6; c = 1;

for k=1:N;
  weight=0;
  NumOfMins = poissrnd(20);
  for i=1:NumOfMins;
    X=0; Y=c;
      while Y > X / exp(X);
        U = rand; V = rand; X = a + (b-a)*U; Y = c*V;
      end;
     weight = weight + X;
   end;
  
  TotalNumOfMinions(k) = weight;
  
end; 

AverageWeight = mean(TotalNumOfMinions);
