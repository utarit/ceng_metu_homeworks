N = 38416;
mu = 2;
a=0; b=1;
TotalNumList = zeros(N, 1);

for k=1:N
   A = exprnd(mu);
   B = normrnd(a,b);
   X = (A+2*B) / (1+ abs(B));
   TotalNumList(k) = X;
 end;
 
 Estimation = mean(TotalNumList);
