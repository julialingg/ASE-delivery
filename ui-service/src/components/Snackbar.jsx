import * as React from "react";
import MuiSnackbar from "@mui/material/Snackbar";
import MuiAlert from "@mui/material/Alert";

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

export default function Snackbar({ message, handleClose }) {
  const onClose = (event, reason) => {
    if (reason === "clickaway") {
      return;
    }
    handleClose();
  };

  return (
    <MuiSnackbar
      open={!!message.content}
      autoHideDuration={3000}
      onClose={onClose}
      anchorOrigin={{ vertical: "bottom", horizontal: "center" }}
    >
      <Alert onClose={onClose} severity={message.severity}>
        {message.content}
      </Alert>
    </MuiSnackbar>
  );
}
