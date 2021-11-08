import React from 'react';
import Portal from "./Portal";

const Remodal = ({ children, close, render }) => {
    return (
        <Portal>
            <div>
                <div style={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    position: "absolute",
                    top: 0,
                    left: 0,
                    width: "100%",
                    height: "100%"
                }}>
                    <div style={{
                        position: "relative",
                        background: "white",
                        borderRadius: "2px",
                        padding: "15px",
                        minWidth: "320px",
                        maxWidth: "600px",
                        maxHeight: "600px",
                        zIndex: 10,
                        boxShadow: "2px 2px 10px rgba(0, 0, 0, 0.3)",
                        marginBottom: "100px"
                    }}>
                        {render(children) || children}
                        <hr/>
                        <button onClick={close}>Close</button>
                        <div>

                        </div>

                        < div style={{
                            position: "absolute",
                            width: "100%",
                            height: "100%",
                            top: 0,
                            left: 0,
                            background: "black",
                            opacity: "0.5"
                        }}
                        />
                    </div>
                </div>
            </div>
        </Portal>
    );
};

export default Remodal;