package com.trinity.manneger.dto;

public record RegisterRequest(
        String name,
        String email,
        String password,
        String role
) {

        public void setPassword(String string) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'setPassword'");
        }

        public void setEmail(String string) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'setEmail'");
        }
        
}
