import { Container, Grid, Paper, Typography } from '@mui/material';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { callUserDetails } from '../Api';
import { UserMainDetails } from './UserMainDetails';
import { UserPreferences } from './UserPreferences';

export const UserPage = () => {
    let { email } = useParams();
    const [userDetails, setUserDetails] = useState(null);

    useEffect(() => {
        callUserDetails(email)
            .then(response => {
                if (!response.ok) {
                    throw new Error(response.status);
                } else {
                    response.json().then(json => {
                        setUserDetails(json);
                    });
                }
            })
            .catch(statusCode => {
                if (statusCode === 401) {
                    // Make sure to handle this higher in the chain
                    window.alert('You are not allowed to query this data');
                } else {
                    window.alert('Unknown error');
                }
            })
    }, []);

    if (!userDetails) {
        return null;
    } else {
        return (
            <div id="userPage" data-test-id="user_page">
                <Container maxWidth="lg" id="userPageContainer">
                    <Paper sx={{ overflow: 'hidden', my: '2rem', padding: '0.5rem' }} id="userPagePaper">
                        <Typography variant="h4" data-test-id="email_title">
                            {userDetails.email}
                        </Typography>
                    </Paper>
                    <Grid container spacing={6}>
                        <Grid item xs={6}>
                            <UserMainDetails userDetails={userDetails} />
                        </Grid>
                        <Grid item xs={6}>
                            <UserPreferences preferences={userDetails.preferences} />
                        </Grid>
                    </Grid>
                </Container>
            </div>
        )
    }
}