import React, { useEffect, useState } from "react";
import {
  Box,
  Button,
  Container,
  TextField,
  Card,
  CardContent,
  Divider,
  IconButton,
  Typography,
  Stack,
} from "@mui/material";
import ClearIcon from "@mui/icons-material/Clear";
import authService from "../../services/authService";
import deliveryService from "../../services/deliveryService";

export default function CustomerActiveDeliveriesView() {
  const [deliveries, setDeliveries] = useState([]);
  const [code, setCode] = useState("");
  const [tracked, setTracked] = useState(false);

  useEffect(() => {
    const user = authService.getUser();
    const email = user.username;
    deliveryService
      .listCustomerActiveDeliveries(email)
      .then((deliveries) => setDeliveries(deliveries));
  }, []);

  const track = () => {
    const index = deliveries.findIndex(({ id }) => id === code);
    if (index > -1)
      deliveryService.get(code).then((delivery) => {
        const newValues = [...deliveries];
        newValues[index] = delivery;
        setDeliveries(newValues);
        setTracked(true);
        var element = document.getElementById(code);
        element.scrollIntoView();
      });
  };

  return (
    <Container
      sx={{
        display: "flex",
        justifyContent: "center",
        alignContent: "center",
        alignItems: "stretch",
        width: "70%",
        flexDirection: "column",
      }}
    >
      <Box
        sx={{
          my: 2,
          display: "flex",
          justifyContent: "center",
          alignContent: "center",
          alignItems: "center",
        }}
      >
        <TextField
          label="Delivery ID"
          size="small"
          key="tracking-code"
          onChange={(e) => {
            if (tracked) setTracked(false);
            setCode(e.target.value);
          }}
          value={code}
          InputProps={{
            endAdornment: (
              <IconButton sx={{ visibility: code ? "visible" : "hidden" }}>
                <ClearIcon onClick={() => setCode("")} />
              </IconButton>
            ),
          }}
          sx={{
            width: "50%",
            "& div": {
              borderTopRightRadius: 0,
              borderBottomRightRadius: 0,
              paddingRight: 0,
            },
            "& fieldset": {
              borderColor: "#1976d2",
              borderWidth: "1.5px",
            },
            "&:hover": {
              "&& fieldset": {
                borderColor: "#1976d2",
              },
            },
          }}
        />
        <Button
          variant="contained"
          onClick={track}
          key="tracking-code-button"
          sx={{
            padding: "8px 17px",
            marginTop: "-0.25px",
            borderTopLeftRadius: 0,
            borderBottomLeftRadius: 0,
            boxShadow: 0,
            "&:hover": {
              boxShadow: 0,
            },
          }}
        >
          Track
        </Button>
      </Box>
      <Stack spacing={5}>
        {deliveries.map(({ id, boxName, status }) => (
          <Card
            sx={{
              minWidth: 275,
              bgcolor: code === id && tracked ? "#F0F7FF" : "#FFF",
            }}
            key={id}
            id={id}
          >
            <CardContent sx={{ px: 3 }}>
              <Typography variant="subtitle1" gutterBottom component="div">
                Delivery ID: {status ? id : "*********************"}
              </Typography>
              <Divider sx={{ mb: 1 }} />
              <Box
                sx={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "center",
                }}
              >
                <Box>
                  <Typography variant="body1" gutterBottom>
                    Box Name: {boxName}
                  </Typography>
                </Box>
                <Typography variant="body1" gutterBottom>
                  Status: {status ? status : "*"}
                </Typography>
              </Box>
            </CardContent>
          </Card>
        ))}
      </Stack>
    </Container>
  );
}
