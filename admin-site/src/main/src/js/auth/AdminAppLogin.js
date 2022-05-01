import { LockOpen } from "@mui/icons-material"
import Box from "@mui/material/Box"
import Button from "@mui/material/Button"
import Container from "@mui/material/Container"
import CssBaseline from "@mui/material/CssBaseline"
import TextField from "@mui/material/TextField"
import Typography from "@mui/material/Typography"
import { useState } from "react"

const loginEnabled = (username, password) => {
    return username && password;
}

function AdminAppLogin() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    return (
        <Container maxWidth="sm" sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center"
        }}>
            <CssBaseline />
            <Box
                sx={{
                    marginTop: 16,
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    bgcolor: 'common.white',
                    width: '100%',
                    p: 8,
                    border: 1,
                    borderColor: 'primary.light',
                    borderRadius: 1,
                }}
            >
                <LockOpen fontSize="large"/>
                <Typography component="h1" variant="h5" marginTop={4}>
                    Sign in
                </Typography>
                <Box sx={{
                    my: 2,
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center"
                }}>
                    <TextField
                        id="username-input"
                        label="Username"
                        variant="outlined"
                        margin="normal" 
                        onChange={event => setUsername(event.target.value)}
                        />
                    <TextField
                        margin="normal"
                        id="password-input"
                        label="Password"
                        type="password"
                        autoComplete="current-password"
                        onChange={event => setPassword(event.target.value)}
                    />
                </Box>
                <Box sx={{
                    my: 2,
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center"
                }}>
                    <Button id="login-button" variant="outlined" disabled={!loginEnabled(username, password)}>Login</Button>
                </Box>
            </Box>
        </Container>
    )
}

export default AdminAppLogin