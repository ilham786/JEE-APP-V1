package com.example.data.curriculum

import com.example.data.model.BlockedAppEntity
import com.example.data.model.ExamTrack
import com.example.data.model.SubjectDomain
import com.example.data.model.SyllabusChapterEntity

object CurriculumData {

    val defaultBlockedApps = listOf(
        BlockedAppEntity("com.instagram.android", "Instagram", "Social Media", isBlocked = true, isDefault = true, domainFallback = "instagram.com"),
        BlockedAppEntity("com.google.android.youtube", "YouTube", "Video & Entertainment", isBlocked = true, isDefault = true, domainFallback = "youtube.com"),
        BlockedAppEntity("com.reddit.frontpage", "Reddit", "Social & Forums", isBlocked = true, isDefault = true, domainFallback = "reddit.com"),
        BlockedAppEntity("com.discord", "Discord", "Gaming & Chat", isBlocked = true, isDefault = true, domainFallback = "discord.com"),
        BlockedAppEntity("com.twitter.android", "X (Twitter)", "Social Media", isBlocked = true, isDefault = true, domainFallback = "x.com"),
        BlockedAppEntity("com.snapchat.android", "Snapchat", "Social Media", isBlocked = true, isDefault = true, domainFallback = "snapchat.com"),
        BlockedAppEntity("com.facebook.katana", "Facebook", "Social Media", isBlocked = true, isDefault = true, domainFallback = "facebook.com"),
        BlockedAppEntity("com.netflix.mediaclient", "Netflix", "Streaming Video", isBlocked = true, isDefault = true, domainFallback = "netflix.com"),
        BlockedAppEntity("com.amazon.avod.thirdpartyclient", "Prime Video", "Streaming Video", isBlocked = true, isDefault = true, domainFallback = "primevideo.com"),
        BlockedAppEntity("com.disney.disneyplus", "Disney+ Hotstar", "Streaming Video", isBlocked = true, isDefault = true, domainFallback = "disneyplus.com"),
        BlockedAppEntity("tv.twitch.android.app", "Twitch", "Live Streaming", isBlocked = true, isDefault = true, domainFallback = "twitch.tv"),
        BlockedAppEntity("com.zhiliaoapp.musically", "TikTok", "Short Video", isBlocked = true, isDefault = true, domainFallback = "tiktok.com"),
        BlockedAppEntity("com.pinterest", "Pinterest", "Media & Photos", isBlocked = true, isDefault = true, domainFallback = "pinterest.com"),
        BlockedAppEntity("com.valvesoftware.android.steam.community", "Steam", "Gaming Community", isBlocked = true, isDefault = true, domainFallback = "steamcommunity.com"),
        BlockedAppEntity("com.ninegag.android.app", "9GAG", "Memes & Distraction", isBlocked = true, isDefault = true, domainFallback = "9gag.com")
    )

    fun getInitialJeeChapters(): List<SyllabusChapterEntity> = listOf(
        // JEE Physics
        SyllabusChapterEntity("jee_p_1", ExamTrack.JEE, SubjectDomain.PHYSICS, "Units & Measurements", 5, 5, 45, isWeakTopic = false, weightage = "Medium"),
        SyllabusChapterEntity("jee_p_2", ExamTrack.JEE, SubjectDomain.PHYSICS, "Kinematics in 1D & 2D", 8, 7, 52, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_p_3", ExamTrack.JEE, SubjectDomain.PHYSICS, "Newton's Laws & Friction", 9, 6, 60, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_p_4", ExamTrack.JEE, SubjectDomain.PHYSICS, "Work, Energy & Power", 7, 5, 48, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_p_5", ExamTrack.JEE, SubjectDomain.PHYSICS, "Rotational Dynamics (Rigid Body)", 12, 6, 68, isWeakTopic = true, weightage = "Very High"),
        SyllabusChapterEntity("jee_p_6", ExamTrack.JEE, SubjectDomain.PHYSICS, "Gravitation & Keplers Laws", 6, 4, 38, isWeakTopic = false, weightage = "Medium"),
        SyllabusChapterEntity("jee_p_7", ExamTrack.JEE, SubjectDomain.PHYSICS, "Thermodynamics & KTG", 10, 8, 55, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_p_8", ExamTrack.JEE, SubjectDomain.PHYSICS, "Simple Harmonic Motion & Waves", 11, 7, 50, isWeakTopic = true, weightage = "High"),
        SyllabusChapterEntity("jee_p_9", ExamTrack.JEE, SubjectDomain.PHYSICS, "Electrostatics & Capacitors", 14, 10, 72, isWeakTopic = false, weightage = "Very High"),
        SyllabusChapterEntity("jee_p_10", ExamTrack.JEE, SubjectDomain.PHYSICS, "Current Electricity", 9, 8, 58, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_p_11", ExamTrack.JEE, SubjectDomain.PHYSICS, "Magnetic Effects of Current & EMI", 12, 7, 65, isWeakTopic = true, weightage = "Very High"),
        SyllabusChapterEntity("jee_p_12", ExamTrack.JEE, SubjectDomain.PHYSICS, "Ray & Wave Optics", 13, 8, 62, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_p_13", ExamTrack.JEE, SubjectDomain.PHYSICS, "Modern Physics & Semiconductors", 10, 9, 80, isWeakTopic = false, weightage = "Very High"),

        // JEE Chemistry
        SyllabusChapterEntity("jee_c_1", ExamTrack.JEE, SubjectDomain.CHEMISTRY, "Mole Concept & Stoichiometry", 6, 6, 50, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_c_2", ExamTrack.JEE, SubjectDomain.CHEMISTRY, "Atomic Structure & Quantum", 8, 7, 46, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_c_3", ExamTrack.JEE, SubjectDomain.CHEMISTRY, "Chemical Bonding & VSEPR", 11, 10, 75, isWeakTopic = false, weightage = "Very High"),
        SyllabusChapterEntity("jee_c_4", ExamTrack.JEE, SubjectDomain.CHEMISTRY, "Chemical & Ionic Equilibrium", 12, 6, 64, isWeakTopic = true, weightage = "Very High"),
        SyllabusChapterEntity("jee_c_5", ExamTrack.JEE, SubjectDomain.CHEMISTRY, "Thermodynamics & Thermochemistry", 9, 7, 54, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_c_6", ExamTrack.JEE, SubjectDomain.CHEMISTRY, "Solutions & Colligative Properties", 7, 6, 42, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_c_7", ExamTrack.JEE, SubjectDomain.CHEMISTRY, "Electrochemistry & Redox", 9, 7, 56, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_c_8", ExamTrack.JEE, SubjectDomain.CHEMISTRY, "Chemical Kinetics", 8, 8, 51, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_c_9", ExamTrack.JEE, SubjectDomain.CHEMISTRY, "Coordination Compounds", 10, 9, 68, isWeakTopic = false, weightage = "Very High"),
        SyllabusChapterEntity("jee_c_10", ExamTrack.JEE, SubjectDomain.CHEMISTRY, "General Organic Chemistry (GOC)", 12, 10, 76, isWeakTopic = true, weightage = "Very High"),
        SyllabusChapterEntity("jee_c_11", ExamTrack.JEE, SubjectDomain.CHEMISTRY, "Hydrocarbons & Alkyl Halides", 10, 7, 58, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_c_12", ExamTrack.JEE, SubjectDomain.CHEMISTRY, "Alcohols, Phenols & Carbonyls", 13, 8, 70, isWeakTopic = true, weightage = "Very High"),
        SyllabusChapterEntity("jee_c_13", ExamTrack.JEE, SubjectDomain.CHEMISTRY, "Amines & Biomolecules", 8, 8, 48, isWeakTopic = false, weightage = "High"),

        // JEE Mathematics
        SyllabusChapterEntity("jee_m_1", ExamTrack.JEE, SubjectDomain.MATHEMATICS, "Quadratic Equations & Inequalities", 7, 7, 55, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_m_2", ExamTrack.JEE, SubjectDomain.MATHEMATICS, "Complex Numbers", 9, 5, 52, isWeakTopic = true, weightage = "High"),
        SyllabusChapterEntity("jee_m_3", ExamTrack.JEE, SubjectDomain.MATHEMATICS, "Sequences & Series", 8, 8, 60, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_m_4", ExamTrack.JEE, SubjectDomain.MATHEMATICS, "Permutations, Combinations & Probability", 12, 6, 68, isWeakTopic = true, weightage = "Very High"),
        SyllabusChapterEntity("jee_m_5", ExamTrack.JEE, SubjectDomain.MATHEMATICS, "Matrices & Determinants", 8, 8, 62, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_m_6", ExamTrack.JEE, SubjectDomain.MATHEMATICS, "Straight Lines & Circles", 11, 9, 70, isWeakTopic = false, weightage = "Very High"),
        SyllabusChapterEntity("jee_m_7", ExamTrack.JEE, SubjectDomain.MATHEMATICS, "Conic Sections (Parabola/Ellipse/Hyperbola)", 12, 6, 64, isWeakTopic = true, weightage = "Very High"),
        SyllabusChapterEntity("jee_m_8", ExamTrack.JEE, SubjectDomain.MATHEMATICS, "Limits, Continuity & Differentiability", 10, 9, 65, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_m_9", ExamTrack.JEE, SubjectDomain.MATHEMATICS, "Applications of Derivatives (AOD)", 11, 7, 58, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_m_10", ExamTrack.JEE, SubjectDomain.MATHEMATICS, "Definite Integrals & Area", 12, 8, 74, isWeakTopic = true, weightage = "Very High"),
        SyllabusChapterEntity("jee_m_11", ExamTrack.JEE, SubjectDomain.MATHEMATICS, "Differential Equations", 8, 7, 53, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("jee_m_12", ExamTrack.JEE, SubjectDomain.MATHEMATICS, "Vectors & 3D Geometry", 11, 10, 82, isWeakTopic = false, weightage = "Very High")
    )

    fun getInitialNeetChapters(): List<SyllabusChapterEntity> = listOf(
        // NEET Physics (Sample subset)
        SyllabusChapterEntity("neet_p_1", ExamTrack.NEET, SubjectDomain.PHYSICS, "Units & Measurements", 5, 5, 38, isWeakTopic = false, weightage = "Medium"),
        SyllabusChapterEntity("neet_p_2", ExamTrack.NEET, SubjectDomain.PHYSICS, "Motion & Laws of Motion", 9, 8, 55, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("neet_p_3", ExamTrack.NEET, SubjectDomain.PHYSICS, "Work, Energy & Gravitation", 9, 7, 48, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("neet_p_4", ExamTrack.NEET, SubjectDomain.PHYSICS, "Thermodynamics & Fluids", 10, 7, 52, isWeakTopic = true, weightage = "High"),
        SyllabusChapterEntity("neet_p_5", ExamTrack.NEET, SubjectDomain.PHYSICS, "Current Electricity & Magnetism", 12, 9, 65, isWeakTopic = false, weightage = "Very High"),
        SyllabusChapterEntity("neet_p_6", ExamTrack.NEET, SubjectDomain.PHYSICS, "Optics & Modern Physics", 13, 10, 74, isWeakTopic = false, weightage = "Very High"),

        // NEET Chemistry
        SyllabusChapterEntity("neet_c_1", ExamTrack.NEET, SubjectDomain.CHEMISTRY, "Atomic Structure & Periodic Table", 8, 8, 52, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("neet_c_2", ExamTrack.NEET, SubjectDomain.CHEMISTRY, "Chemical Bonding & Molecular Structure", 10, 9, 70, isWeakTopic = false, weightage = "Very High"),
        SyllabusChapterEntity("neet_c_3", ExamTrack.NEET, SubjectDomain.CHEMISTRY, "Equilibrium & Thermodynamics", 10, 6, 58, isWeakTopic = true, weightage = "High"),
        SyllabusChapterEntity("neet_c_4", ExamTrack.NEET, SubjectDomain.CHEMISTRY, "Coordination Chemistry & p/d/f Block", 12, 10, 68, isWeakTopic = false, weightage = "Very High"),
        SyllabusChapterEntity("neet_c_5", ExamTrack.NEET, SubjectDomain.CHEMISTRY, "Organic Chemistry Basics & Functional Groups", 13, 9, 75, isWeakTopic = true, weightage = "Very High"),
        SyllabusChapterEntity("neet_c_6", ExamTrack.NEET, SubjectDomain.CHEMISTRY, "Biomolecules & Polymers", 7, 7, 44, isWeakTopic = false, weightage = "High"),

        // NEET Botany
        SyllabusChapterEntity("neet_b_1", ExamTrack.NEET, SubjectDomain.BOTANY, "Cell: The Unit of Life & Cell Division", 9, 9, 72, isWeakTopic = false, weightage = "Very High"),
        SyllabusChapterEntity("neet_b_2", ExamTrack.NEET, SubjectDomain.BOTANY, "Plant Kingdom & Diversity", 8, 7, 55, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("neet_b_3", ExamTrack.NEET, SubjectDomain.BOTANY, "Morphology & Anatomy of Flowering Plants", 11, 8, 62, isWeakTopic = true, weightage = "High"),
        SyllabusChapterEntity("neet_b_4", ExamTrack.NEET, SubjectDomain.BOTANY, "Photosynthesis & Plant Respiration", 10, 8, 66, isWeakTopic = false, weightage = "Very High"),
        SyllabusChapterEntity("neet_b_5", ExamTrack.NEET, SubjectDomain.BOTANY, "Sexual Reproduction in Flowering Plants", 8, 8, 68, isWeakTopic = false, weightage = "Very High"),
        SyllabusChapterEntity("neet_b_6", ExamTrack.NEET, SubjectDomain.BOTANY, "Principles of Inheritance & Variation", 12, 9, 85, isWeakTopic = true, weightage = "Very High"),
        SyllabusChapterEntity("neet_b_7", ExamTrack.NEET, SubjectDomain.BOTANY, "Molecular Basis of Inheritance", 13, 10, 92, isWeakTopic = true, weightage = "Very High"),
        SyllabusChapterEntity("neet_b_8", ExamTrack.NEET, SubjectDomain.BOTANY, "Ecology: Organisms, Populations & Ecosystem", 10, 9, 64, isWeakTopic = false, weightage = "High"),

        // NEET Zoology
        SyllabusChapterEntity("neet_z_1", ExamTrack.NEET, SubjectDomain.ZOOLOGY, "Animal Kingdom Classification", 10, 8, 65, isWeakTopic = true, weightage = "Very High"),
        SyllabusChapterEntity("neet_z_2", ExamTrack.NEET, SubjectDomain.ZOOLOGY, "Human Physiology: Digestion & Respiration", 9, 8, 58, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("neet_z_3", ExamTrack.NEET, SubjectDomain.ZOOLOGY, "Circulation, Excretion & Locomotion", 11, 9, 70, isWeakTopic = false, weightage = "Very High"),
        SyllabusChapterEntity("neet_z_4", ExamTrack.NEET, SubjectDomain.ZOOLOGY, "Neural & Chemical Coordination (Endocrine)", 10, 8, 62, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("neet_z_5", ExamTrack.NEET, SubjectDomain.ZOOLOGY, "Human Reproduction & Reproductive Health", 9, 9, 76, isWeakTopic = false, weightage = "Very High"),
        SyllabusChapterEntity("neet_z_6", ExamTrack.NEET, SubjectDomain.ZOOLOGY, "Evolution & Human Health and Disease", 11, 9, 68, isWeakTopic = false, weightage = "High"),
        SyllabusChapterEntity("neet_z_7", ExamTrack.NEET, SubjectDomain.ZOOLOGY, "Biotechnology: Principles & Applications", 9, 8, 74, isWeakTopic = false, weightage = "Very High")
    )
}
