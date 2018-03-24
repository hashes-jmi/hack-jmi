pragma solidity ^0.4.11;

contract KnowledgeToken {
    mapping (address => uint256) public balanceOf;// This creates an array with all balances
    string public name;
    string public symbol;
    uint8 public decimals;
     
    /* Initializes contract with initial supply tokens to the creator of the contract */
    function KnowledgeToken(
        uint256 initialSupply
        ) {
        balanceOf[msg.sender] = initialSupply;              // Give the creator all initial tokens
        name = "Knowledge Tokens";
        symbol = "KTN";
        decimals = 2;
        }
        
    function getTokens(){
        
    }
    
    function buyFromStore(uint256 _val){
        
        balanceOf[msg.sender]-=_val;
    }
}