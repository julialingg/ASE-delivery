import React from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
} from "@mui/material";

export default function ConfirmationDialog({
  open,
  title,
  children,
  onClose,
  onConfirm,
}) {
  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle sx={{ pr: 10 }}>{title}</DialogTitle>
      <DialogContent sx={{ pl: 5 }}>{children}</DialogContent>
      <DialogActions sx={{ pr: 2, pb: 2 }}>
        <Button onClick={onClose}>Cancel</Button>
        <Button onClick={onConfirm} variant="contained">
          Confirm
        </Button>
      </DialogActions>
    </Dialog>
  );
}
