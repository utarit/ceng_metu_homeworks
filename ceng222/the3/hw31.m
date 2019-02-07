N = 38416;
lambda = 20;
ProbOfCond= 7/27;
TotalNumOfMinions = zeros(N, 1);
for k=1:N;
    U = poissrnd(20);
    X = sum( rand(U, 1) < ProbOfCond);
    
    TotalNumOfMinions(k) = X;

end;

P = mean(TotalNumOfMinions > 6);