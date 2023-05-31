import React, { useEffect, useState } from "react";
import {
  AppBar,
  Button,
  CssBaseline,
  Toolbar,
  Typography,
} from "@mui/material";
import HomeView from "./views/HomeView";
import LoginView from "./views/LoginView";
import AuthService from "./services/authService";

function App() {
  const [user, setUser] = useState({});

  const onLogout = () => {
    setUser({});
    AuthService.logout();
  };

  useEffect(() => {
    setUser(AuthService.getUser());
  }, []);

  return (
    <React.Fragment>
      <CssBaseline />
      <AppBar position="static">
        <Toolbar
          variant="dense"
          sx={{ display: "flex", justifyContent: "space-between" }}
        >
          <Typography
            variant="h6"
            noWrap
            component="div"
            sx={{
              fontFamily: "monospace",
              fontWeight: 700,
              letterSpacing: ".15rem",
            }}
          >
            ASE Delivery
          </Typography>
          {user.role && (
            <Button color="inherit" onClick={onLogout}>
              Log out
            </Button>
          )}
        </Toolbar>
      </AppBar>
      {user.role ? (
        <HomeView role={user.role} />
      ) : (
        <LoginView setUser={setUser} />
      )}
    </React.Fragment>
  );
}

export default App;
