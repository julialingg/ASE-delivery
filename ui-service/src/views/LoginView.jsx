import React, { useState } from "react";
import {
  Button,
  Container,
  IconButton,
  Input,
  InputLabel,
  InputAdornment,
  FormControl,
  FormHelperText,
  Paper,
  TextField,
  Typography,
} from "@mui/material";
import { Visibility, VisibilityOff } from "@mui/icons-material";
import Form from "../components/form/Form";
import AuthService from "../services/authService";

export default function LoginView({ setUser }) {
  const [showPassword, setShowPassword] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  
  const handleLogin = async (data) => {
    try {
      await AuthService.login(data);
      setUser(AuthService.getUser);
    } catch (err) {
      console.log(err);
      setErrorMessage(err);
    }
  };

  return (
    <Container
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        pt: 10,
      }}
    >
      <Paper
        elevation={2}
        sx={{
          width: "480px",
          px: 7,
          pt: 4,
          pb: 5,
          textAlign: "center",
        }}
      >
        <Form
          onSubmit={handleLogin}
          sx={{
            "& > :not(style)": { my: 1.5 },
          }}
        >
          <Typography
            variant="h5"
            sx={{
              fontFamily: "monospace",
              fontWeight: 700,
              letterSpacing: ".15rem",
            }}
          >
            Login
          </Typography>
          <TextField
            required
            name="username"
            label="Email"
            type="text"
            fullWidth
            variant="standard"
          />
          <FormControl variant="standard" fullWidth>
            <InputLabel htmlFor="user-password">Password</InputLabel>
            <Input
              required
              name="password"
              type={showPassword ? "text" : "password"}
              endAdornment={
                <InputAdornment position="end">
                  <IconButton
                    aria-label="toggle password visibility"
                    onClick={() => setShowPassword(!showPassword)}
                    onMouseDown={(e) => e.preventDefault()}
                  >
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              }
            />
          </FormControl>
          {errorMessage && (
            <FormHelperText error>{errorMessage}</FormHelperText>
          )}
          <Button type="submit" variant="contained" fullWidth>
            Log in
          </Button>
        </Form>
      </Paper>
    </Container>
  );
}
