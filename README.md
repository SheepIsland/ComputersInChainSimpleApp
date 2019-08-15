# ComputersInChainSimpleApp
Simple Application coining computers in chain 

Algorithm :

    1. There are local vars for counting left, right hosts.
    
    2. Initially left_count = 0, right_count = 0
    
    3. Client send 1 to left and right hosts.
       This way hosts learn about each other.
       
    3. Each server is waiting for a message from left,right. 
    
    4. When a server gets a message from left:
    
        4.1 Server compares left_count from getting_count(count from message).
        
        4.2 If getting_count > left_count than left_count = getting_count.
            This way server understands that there are more left hosts.
            
        4.3 Server sends getting_count right down the chain.
        
    5. When a server gets a message from right it compares etc.
    
    6. Hosts exchange messages such way until their left_count, right_count becomes equal to received counts.
   
    
