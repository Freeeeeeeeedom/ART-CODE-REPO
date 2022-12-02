package dt.original;

public class backpack {
	   public int exe(int[] v_1, int[] w_1, int V) {
	    // 二维dp
	    int N = v_1.length;
	    int[] v = new int[N + 1];
	    int[] w = new int[N + 1];
	    for(int i = 0; i < N; i++) {
	    	v[i + 1] = v_1[i];
	    	w[i+1] = w_1[i];
	    }
	    int[][] dp = new int[N + 1][V + 1];
	    dp[0][0] = 0;
	    //此处i从1开始是因为上面v[i]和w[i]是从1开始存的
	    for(int i = 1; i <= N; i++){
	        for(int j = 0; j <= V; j++){
	           if(j >= v[i]){
	                dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-v[i]] + w[i]);
	           }else{
	               dp[i][j] = dp[i-1][j];
	           }
	       }
	   }
	    int[] dp_1 = new int[V+1];
	    dp_1[0] = 0;
	    for(int i = 1; i <= N; i++){
	        for(int j = V; j >= v[i]; j--){
	            dp_1[j] = Math.max(dp_1[j], dp_1[j-v[i]] + w[i]);
	        }
	    }
	    return dp_1[V];
	}
	   
}

