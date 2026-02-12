package com.trinity.manneger.dto;

public record LoginRequest(
        String email,
        String password) {

        public void setEmail(String string) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'setEmail'");
        }

        public void setPassword(String string) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'setPassword'");
        }
}
